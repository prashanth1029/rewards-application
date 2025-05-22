Overview
--------
The Rewards Controller provides the following capabilities:
Create a customer reward
Retrieve all rewards
Delete or update a reward by ID
Calculate reward points for a customer
Calculate reward points for all customers

DTOs & Entities
---------------
CustomerReward: Entity representing a transaction
RewardResponseDTO: DTO representing reward points grouped by month and total

Validation & Error Handling
---------------------------
Uses @Valid on request bodies to enforce field-level validation
Returns 400 Bad Request for invalid inputs, MethodArgumentNotValidException
Custom exceptions (like CustomerRewardNotFoundException, CustomerNotFoundException) mapped to 404

check endpoints documentation using swagger at - http://localhost:8080/swagger-ui/index.html

PUT
/rewards/editReward/{id}
Update an existing transaction by ID

Request body for editing reward
application/json
Example Value
Schema
{
"customerId": "cust123",
"amount": 120.5,
"transactionDate": "2024-03-15"
}

Responses
Code	Description	Links
200
Successfully retrieved rewards

Media type

application/json
Controls Accept header.
Example Value
Schema
{
"customerId": "string",
"monthlyPoints": {
"additionalProp1": 0,
"additionalProp2": 0,
"additionalProp3": 0
},
"totalPoints": 0
}

===========================================================================
POST
/rewards/createReward
Add a new customer transaction

Request body
application/json
Example Value
Schema
{
"customerId": "cust123",
"amount": 120.5,
"transactionDate": "2024-03-15"
}

Responses
Code	Description	Links
200
Successfully retrieved rewards

Media type
application/json
Controls Accept header.
Example Value
Schema
{
"customerId": "string",
"monthlyPoints": {
"additionalProp1": 0,
"additionalProp2": 0,
"additionalProp3": 0
},
"totalPoints": 0
}
============================================================================
GET
/rewards
Get reward points for all customers

Responses
Code	Description	Links
200
Successfully retrieved rewards

Media type

application/json
Controls Accept header.
Example Value
Schema
{
"customerId": "string",
"monthlyPoints": {
"additionalProp1": 0,
"additionalProp2": 0,
"additionalProp3": 0
},
"totalPoints": 0
}

=====================================================================
GET
/rewards/{customerId}
Get reward points for a specific customer

Responses
Code	Description	Links
200
Successfully retrieved rewards

Media type

application/json
Controls Accept header.
Example Value
Schema
{
"customerId": "string",
"monthlyPoints": {
"additionalProp1": 0,
"additionalProp2": 0,
"additionalProp3": 0
},
"totalPoints": 0
}

=======================================================================
GET
/rewards/getAllRewards
Get all customer transactions

Responses
Code	Description	Links
200
Successfully retrieved rewards

Media type

application/json
Controls Accept header.
Example Value
Schema
{
"customerId": "string",
"monthlyPoints": {
"additionalProp1": 0,
"additionalProp2": 0,
"additionalProp3": 0
},
"totalPoints": 0
}

=====================================================================
DELETE
/rewards/deleteReward/{id}
Delete a customer transaction by ID

Responses
Code	Description	Links
200
Successfully retrieved rewards

Media type

application/json
Controls Accept header.
Example Value
Schema
{
"customerId": "string",
"monthlyPoints": {
"additionalProp1": 0,
"additionalProp2": 0,
"additionalProp3": 0
},
"totalPoints": 0
}

