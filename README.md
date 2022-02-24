# Solution for Massive Consumption Company Challenge

This is my proposed solution for the Massive Consumption Company challenge completed for the interview process.

In which the requirements were:

1. An API is needed that provides a search service to list all the products found from a Mongo database.
2. In case the search is a palindrome, the products must be returned with the 50% discount already applied to the price.
3. When it is a search on product identifiers, the exact result must be returned, that is, only one product.
4. For the brand and product description, it is enough that the search is longer than 3 characters and that these are included in the aforementioned fields (brand and description).

I have created a REST API which has three endpoints:

- /getAllProducts/ : which will get all products listed in the database.
- /getProductById/{id} : which will get a single product fot that id.
- /getProductByBrandAndDescription?desc={product} : which will return all the products that contain the search term in either in their description or brand. 

If the search is a palindrome, the price of the products that belong to the result of the query will have their price discounted in half as required.
Maven is my favorite build automation tool and has been used in this project. 
Integration and unit tests have been developed according to quality standards having up to 85% of lines covered.

To run this solution:
You should move to the root directory where the ´docker-compose.yml´ file is located and run the ´docker-compose up´ command.

The following images will be downloaded from DockerHub:
- nachofmartinez/spring-massive-consumption-company:latest
- mongo:latest

After that both containers will start and the spring-massive-consumption-company one will populate the mongo database container with test data. 
Execute ´docker ps´ to find which port the service is located in and curl that port using the endpoints provided. 
