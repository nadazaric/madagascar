# Madagascar
Simplified version of Google's Zanzibar system, providing a flexible configuration language for access control policies, evaluating access control lists (ACLs) and ensuring consistent, scalable and low-latency authorization decisions.
# Running the project

1. **Docker**

Go to project root and run command:
```
docker-compose up -d
```

2. **Angular**

Go to `front` folder and run command:
 ```
ng serve
 ```

3. **SpringBoot**

Go to `back` folder and run the project.

4. **Mini zanzibar**
   
Go to `zanzibar` folder. Firstly, install `venv` using next commands:

 ```
py -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
 ```

Then run command to start mini zanzibar:
 ```
python app.py
 ```

5. **Final**

Go to `localhost:4200` and enjoy!
