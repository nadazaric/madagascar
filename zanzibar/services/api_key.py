import secrets
import hashlib
import os

def generate_api_key():
    api_key = secrets.token_hex()
    hashed_key = hashlib.sha256(api_key.encode()).hexdigest()

    data_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '../data'))
    if not os.path.exists(data_dir):
        os.makedirs(data_dir)
    with open(os.path.join(data_dir, "api_keys.txt"), 'a') as file:
        file.write(f"{hashed_key}\n")

    return api_key