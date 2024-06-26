import plyvel

try:
    db = plyvel.DB('tmp', create_if_missing=True)
    # Use db here
    db.put(b'key', b'value')    
    value = db.get(b'key')
finally:
    if db is not None:
        db.close()
print(value) # b'value'