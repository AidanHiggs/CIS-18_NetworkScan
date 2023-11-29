import pandas as pd
import numpy as np
import re
from sklearn.model_selection import train_test_split, RandomizedSearchCV
from sklearn.preprocessing import LabelEncoder, RobustScaler
from sklearn.metrics import accuracy_score, classification_report, roc_auc_score
from xgboost import XGBClassifier
from joblib import dump
from imblearn.pipeline import Pipeline as IMBPipeline
from imblearn.over_sampling import SMOTE

def extract_features(raw_line):
    """ Extracts and returns a dictionary of features from a raw fingerprint line. """
    features = {}
    pattern = r'(\w+)\(([^)]+)\)'
    matches = re.findall(pattern, raw_line)
    for key, value in matches:
        sub_feats = value.split('%')
        for sub_feat in sub_feats:
            if '=' in sub_feat:
                sub_key, sub_value = sub_feat.split('=')
                features[f'{key}_{sub_key}'] = sub_value
    return features

def parse_fingerprint(fingerprint_data):
    """ Parses the Nmap fingerprint data into a structured DataFrame. """
    parsed_data = []
    current_entry = {}
    for line in fingerprint_data.split('\n'):
        line = line.strip()
        if line.startswith('Fingerprint'):
            if current_entry:
                parsed_data.append(current_entry)
            current_entry = {'Label': line.split(' ')[1]}
        elif line and not line.startswith('#') and '(' in line:
            current_entry.update(extract_features(line))
    if current_entry:
        parsed_data.append(current_entry)
    return pd.DataFrame(parsed_data)

def preprocess_data(data):
    """ Preprocesses the data by encoding categorical variables and scaling numeric values. """
    label_encoder = LabelEncoder()
    data['Label'] = label_encoder.fit_transform(data['Label'])

    for col in data.columns:
        if data[col].dtype == 'object':
            data[col] = pd.to_numeric(data[col], errors='coerce')

        max_float = np.finfo(np.float64).max / 2
        data[col].replace([np.inf, -np.inf], max_float, inplace=True)
        data[col].fillna(0, inplace=True)
        data[col] = np.clip(data[col], np.finfo(np.float32).min, np.finfo(np.float32).max)

    return data, label_encoder

def re_encode_labels(train_labels, test_labels):
    """ Re-encodes labels to ensure they form a continuous sequence starting from 0. """
    all_labels = pd.concat([train_labels, test_labels])
    label_encoder = LabelEncoder()
    label_encoder.fit(all_labels)
    return label_encoder.transform(train_labels), label_encoder.transform(test_labels)

# Load and preprocess data
file_path = "/home/aidan/Documents/CIS-18_NetworkScan/src/main/resources/com/networkscan/cis18/svn.nmap.org_nmap_nmap-os-db.txt"
data = parse_fingerprint(open(file_path).read())
data, label_encoder = preprocess_data(data)

# Split data into features and labels
labels = data['Label']
features = data.drop('Label', axis=1)

# Split data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(features, labels, test_size=0.2, random_state=42)

# Re-encode the labels to ensure continuity
y_train, y_test = re_encode_labels(y_train, y_test)

# SMOTE for handling imbalanced classes
smote = SMOTE()

# XGBoost Classifier with a more extensive hyperparameter search
xgb_classifier = XGBClassifier(use_label_encoder=False, eval_metric='logloss')

# Pipeline with SMOTE and XGBClassifier
pipeline = IMBPipeline([
    ('scaler', RobustScaler()),
    ('smote', smote),
    ('classifier', xgb_classifier)
])

# Hyperparameter tuning with RandomizedSearchCV
parameters = {
    'classifier__n_estimators': [100, 200, 300],
    'classifier__max_depth': [3, 5, 7, 10],
    'classifier__learning_rate': [0.01, 0.05, 0.1, 0.2],
    'classifier__subsample': [0.6, 0.7, 0.8, 0.9, 1],
    'classifier__colsample_bytree': [0.6, 0.7, 0.8, 0.9, 1]
}

random_search = RandomizedSearchCV(pipeline, parameters, cv=3, scoring='accuracy', n_iter=50, random_state=42)
random_search.fit(X_train, y_train)

best_pipeline = random_search.best_estimator_

# Predict on the test set using the best found parameters
predictions = best_pipeline.predict(X_test)

# Evaluate the model with additional metrics
print("Accuracy:", accuracy_score(y_test, predictions))
print("ROC AUC Score:", roc_auc_score(y_test, best_pipeline.predict_proba(X_test)[:, 1]))
print(classification_report(y_test, predictions))

# Save the trained model and label encoder for later use
dump(best_pipeline, 'os_detection_model.joblib')
dump(label_encoder, 'label_encoder.joblib')
