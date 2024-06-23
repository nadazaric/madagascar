import consul

# Initialize the Consul client
c = consul.Consul(host='localhost', port=8500)

# Register a service
c.agent.service.register('my-service', service_id='my-service-1', address='127.0.0.1', port=5000)

# List all registered services
services = c.agent.services()
print(services)

# Put a key-value pair in the Consul KV store
c.kv.put('foo', 'bar')

# Get a value from the Consul KV store
index, data = c.kv.get('foo')
print(data['Value'].decode())  # Output should be 'bar'
