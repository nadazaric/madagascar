import plyvel

db = plyvel.DB('./tmp', create_if_missing=True)
db.put(b'key', b'value')
value = db.get(b'key')
print(value) # b'value'