import secrets
import hashlib
import plyvel
from flask import request, jsonify
from config import LEVELDB_NAME, LEVELDB_PREFIX_API_KEY

db = None

def generate_api_key():
    global db
    api_key = secrets.token_hex()
    try:
        db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
        prefixed_db = db.prefixed_db(LEVELDB_PREFIX_API_KEY)
        prefixed_db.put(hash_api_key(api_key).encode(), b'')
    finally:
        if db is not None:
            db.close()
    return api_key

def hash_api_key(api_key):
    return hashlib.sha256(api_key.encode()).hexdigest()

def require_api_key(func):
    def wrapper(*args, **kwargs):
        global db
        api_key = request.headers.get('x-api-key')
        if not api_key: return jsonify({"message": "Invalid or missing API key"}), 401

        try:
            db = plyvel.DB(LEVELDB_NAME, create_if_missing=True)
            prefixed_db = db.prefixed_db(LEVELDB_PREFIX_API_KEY)
            value = prefixed_db.get(hash_api_key(api_key).encode())
            if value is None: return jsonify({"message": "Invalid or missing API key"}), 401
        finally:
            if db is not None:
                db.close()

        return func(*args, **kwargs)
    return wrapper