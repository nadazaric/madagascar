import secrets
import hashlib
import os
from flask import request, jsonify

def generate_api_key():
    api_key = secrets.token_hex()

    data_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '../data'))
    if not os.path.exists(data_dir):
        os.makedirs(data_dir)
    with open(os.path.join(data_dir, "api_keys.txt"), 'a') as file:
        file.write(f"{hash_api_key(api_key)}\n")

    return api_key

def hash_api_key(api_key):
    return hashlib.sha256(api_key.encode()).hexdigest()

def get_valid_api_keys():
    data_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '../data'))
    if not os.path.exists(data_dir): return []
    api_keys = []
    with open(os.path.join(data_dir, "api_keys.txt"), 'r') as file:
        for line in file.readlines():
            api_keys.append(line.strip())
    return api_keys

def require_api_key(func):
    def wrapper(*args, **kwargs):
        api_key = request.headers.get('madagascar-api-key')
        if not api_key or hash_api_key(api_key) not in get_valid_api_keys():
            response = jsonify({"message": "Invalid or missing API key"}), 401
            return response
        return func(*args, **kwargs)
    return wrapper