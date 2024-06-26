import logging
import logging.config
import html
from flask import request

def log_request(request, level=logging.INFO):
    log_data = {
        'ip': request.remote_addr,
        'method': request.method,
        'url': sanitize_input(request.url),
        'data': sanitize_input(request.get_json() if request.is_json else 'non-JSON data')
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
