import logging
import logging.config
import html
from flask import request
from cryptography.fernet import Fernet
import base64
import json
from dotenv import load_dotenv
import os

load_dotenv()

class EncryptedLogFormatter():
    def __init__(self):
        key = self._load_or_generate_key()
        self.cipher = Fernet(key)

    def encrypt(self, message):
        encrypted_message = self.cipher.encrypt(message.encode()).decode()
        return encrypted_message

    def decrypt(self, encrypted_message):
        decrypted_message = self.cipher.decrypt(encrypted_message.encode()).decode()
        return decrypted_message
    
    def _load_or_generate_key(self):
        key_from_env = os.getenv('ENCRYPT_KEY')
        if key_from_env:
            return key_from_env.encode()

        new_key = Fernet.generate_key()
        with open('.env', 'a') as f:
                f.write(f'\nENCRYPT_KEY={new_key.decode()}')
                os.environ['ENCRYPT_KEY'] = new_key.decode()
        return new_key

def log_request(request, logger_encryptor, level=logging.INFO):
    log_data = {
        'ip': request.remote_addr,
        'method': request.method,
        'url': sanitize_input(request.url),
        'data': logger_encryptor.encrypt(json.dumps(sanitize_input(request.get_json() if request.is_json else 'non-JSON data')))
    }
    app_logger.log(level, f"Request: {log_data}")

def sanitize_input(input_data):
    if isinstance(input_data, str):
        return html.escape(input_data)
    elif isinstance(input_data, dict):
        return {k: sanitize_input(v) for k, v in input_data.items()}
    elif isinstance(input_data, list):
        return [sanitize_input(i) for i in input_data]
    else:
        return input_data


class RequestFilter(logging.Filter):
    def filter(self, record):
        record.ip = request.remote_addr if request else 'N/A'
        return True
    
def configure_logging():
    logging.config.fileConfig('logs/logging_config.ini')

    security_logger = logging.getLogger('security')
    app_logger = logging.getLogger('application')
    request_filter = RequestFilter()

    root_logger = logging.getLogger()
    root_logger.addFilter(request_filter)

    security_logger.addFilter(request_filter)
    app_logger.addFilter(request_filter)

    werkzeug_logger = logging.getLogger('werkzeug')
    werkzeug_logger.setLevel(logging.ERROR)

if not logging.getLogger().hasHandlers():
    configure_logging()

security_logger = logging.getLogger('security')
app_logger = logging.getLogger('application')
