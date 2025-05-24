# Price Comparator Market
The backend for a 
"Price Comparator - Market" application. 
A service that helps users compare prices of 
everyday grocery items across different supermarket 
chains (e.g., Lidl, Mega Image, Metro, Carrefour). 
The system allows users to track price changes, 
find the best deals, and manage their shopping lists effectively.

## Project Structure Overview

- `src/main/java/code/PriceComparatorMarket`: Contains the main source code, including controllers, repositories, models, parsers and services.
- `src/main/java/resources/static`: Contains the csv files from where products and discounts are read and mapped; includes price-alert.csv.
- `pom.xml`: Lists dependencies.
- `README.md`: Project documentation and instructions.


- **Controllers**: are taking care of listening to separate endpoints sent from frontend (e.g. Postman)

- **Models**: are the blueprint of CSV data

- **Parsers**: are responsible to read the data from a CSV and map it to a class (e.g. Product, ProductDiscount)

- **Repositories**: are imitating a database repository, and are responsible to retrieve the data from the CSV files

- **Requests**: are DTOs used to send requests

- **Services**: are responsible for the business logic of the application

## Instructions on how to build and run the application
1. Clone the repo: 
```
git clone https://github.com/pavelmihai96/PriceComparatorMarket
```
2. Open project in IntelliJ
3. Install dependencies while on folder path in terminal:
```
mvn clean install
```
4. Run the application from inside PriceComparatorMarketApplication class
5. Open Postman in order to be able to send requests to the running application

## Assumptions made
- Each store will have its offer of products uploaded once a week (e.g. on 12<sup>th</sup> May and then on 19<sup>th</sup> May; 7 days difference between them), thus some of the application logic is dependent on that.
- Discounts are also uploaded once a week.
- CSVs with products will always have the same format, and CSVs with product discounts will always have the same format.
- All `date` variables sent from requests need to have the same format: `yyyy-mm-dd`.

## How to use the application
All products from all stores are available in AllProducts.xlsx and this file is present in `PriceComparatorMarket` folder. Information from this excel is needed in order to be able to create the requests for each endpoint. Please refer to this file to retrieve product ids, names, brands etc.

### Endpoints

- Daily Shopping Basket Monitoring:
  - Help users split their basket into shopping lists that optimise for cost savings
    1. Paste this in Postman with a POST request: `http://localhost:8081/api/basket/cost-efficient`
    2. In body (raw), an example would be this: `{"productIds": ["P005", "P091", "P137", "P186"], "date": "2025-05-24"}`


- Best Discounts:
  - List products with the highest current percentage discounts across all tracked
  stores.
    1. Paste this in Postman with a GET request: `http://localhost:8081/api/product/discount/highest`
    2. In body (raw), an example would be this: `{"date": "2025-05-12"}`


- New Discounts:
  - List discounts that have been newly added (e.g., within the last 24 hours)
    1. Paste this in Postman with a GET request: `http://localhost:8081/api/product/discount/hours`
    2. In body (raw), an example would be this: `{"hours": 0.5}`. `0.5` means 30 minutes, so the example request verifies the discounts added in the last 30 minutes. `hours` is entered by the user.


- Dynamic Price History Graphs:
  - Provide data points that would allow a frontend to calculate and display price
  trends over time for individual products.
  - This data should be filterable by store, product category, or brand.
  1. Filter by product id:
     1. Paste this in Postman with a GET request: `http://localhost:8081/api/filter/by-productId`
     2. In body (raw), an example would be this: `{"startDate": "2025-05-01", "endDate": "2025-05-25", "productId": "P063"}`
  2. Filter by store:
     1. Paste this in Postman with a GET request: `http://localhost:8081/api/filter/by-store`
     2. In body (raw), an example would be this: `{"startDate": "2025-05-01", "endDate": "2025-05-25", "store": "lidl"}`
  3. Filter by brand:
     1. Paste this in Postman with a GET request: `http://localhost:8081/api/filter/by-brand`
     2. In body (raw), an example would be this: `{"startDate": "2025-05-04", "endDate": "2025-05-25", "brand": "Aro"}`
  4. Filter by category:
      1. Paste this in Postman with a GET request: `http://localhost:8081/api/filter/by-category`
      2. In body (raw), an example would be this: `{"startDate": "2025-05-10", "endDate": "2025-05-15", "category": "lactate"}`


- Product Substitutes & Recommendations:
    - Highlight "value per unit" (e.g., price per kg, price per liter) to help identify the
      best buys, even if the pack size differs.
      1. Paste this in Postman with a POST request: `http://localhost:8081/api/basket/best-buy`
      2. In body (raw), an example would be this: `{"products" : [{"productName":"smântână 20%", "productCategory":"lactate", "brand":"ARO"},{"productName":"iaurt grecesc", "productCategory":"lactate", "brand":"Carrefour"},{"productName":"brânză telemea vacă", "productCategory":"lactate", "brand":"Pilos"},{"productName":"cafea măcinată", "productCategory":"cafea", "brand":"Bellarom"}], "date": "2025-05-20"}`


- Custom Price Alert:
    - Allow users to set a target price for a product. The system should be able to
      identify when a product's price drops to or below that target.
        1. Paste this in Postman with a POST request: `http://localhost:8081/api/basket/price-alert`
        2. In body (raw), an example would be this: `[{"productId": "P005", "priceAlert": 5},{"productId": "P091", "priceAlert": 7.5},{"productId": "P137", "priceAlert": 4.15},{"productId": "P186", "priceAlert": 4.15}]`
    
## Contact
    Email: pavelmihailungu@gmail.com
    LinkedIn: https://www.linkedin.com/public-profile/settings?lipi=urn%3Ali%3Apage%3Ad_flagship3_profile_self_edit_contact-info%3B4UPSOKz3T2C6xpCTjotuiw%3D%3D
    






