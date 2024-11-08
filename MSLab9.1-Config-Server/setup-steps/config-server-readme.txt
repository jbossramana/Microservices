1. Install git software

2. Copy config-server-repo to c:\


3. Now open command prompt from config-server-repo directory and run command
"git init"  to make that directory as git repository.

4. Now run "git add ." to add everything to this repo.

5. Then finally we need to commit the properties file by running command 
git commit -m "initial checkin".

 
6. start the Usecase-Cloud-Config-Server application

5. Verify Server Side Configuration
        http://localhost:8888/PaymentService/development
        http://localhost:8888/OrderService/development
	
6. Start usecase-eureka-server, usecase-eureka-config-client-payment-service, 
Usecase-Eureka-config-client-order-service-CB

Test the client application
----------------------------
http://localhost:8080/
http://localhost:8081/

Test Property Change
------------------------
1. change the msg value in property file

2. point to  http://localhost:8080 &  http://localhost:8081 
Note : value is not changed

3. To reflect the new value, we need to refresh the configuration by hitting
 http://localhost:8080/refresh  & http://localhost:8081/refresh
endpoint using POST method from any of the REST client.




	

	









 