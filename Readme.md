# Overview
This service allows the evaluation of products, tools and frameworks using the [Analyticical Hierachy Process](https://en.wikipedia.org/wiki/Analytic_hierarchy_process).  This services opts to use a Normalized Reciprocal Ranking Algorithm instead of the standard Pairwise Comparison. 

# Running the AHP Service locally
To start the service on `localhost:8080`
```shell
% ./gradlew bootRun
```
To run test
```shell
% ./gradlew test
```

# Running a AHP Analysis
A `POST` to the `/analysis` endpoint will run an analysis.

## Sample `application/vnd.analysis.v1+json` body
```json
{
    "criteria": [
        "cost",
        "reliability"
    ],
    "options": [
        "aws",
        "azure"
    ],
    "optionsCriterion": {
        "cost": [
            "azure",
            "aws"
        ],
        "reliability": [
            "aws",
            "azure"
        ]
    }
}
```
Important items to note about this json body
* Order matters for the `criteria` and `optionsCriterion` lists.  This is used to determine rank in ascending order.
* There is currenlty no validation of this request body when making requests.  

## Sample CURL command
```shell
% curl --location --request POST 'localhost:8080/analysis' \
--header 'Content-Type: application/vnd.analysis.v1+json' \
--header 'Accept: application/vnd.analysis_return.v1+json' \
--data-raw '{
    "criteria": [
        "cost",
        "reliability"
    ],
    "options": [
        "aws",
        "azure"
    ],
    "optionsCriterion": {
        "cost": [
            "azure",
            "aws"
        ],
        "reliability": [
            "aws",
            "azure"
        ]
    }
}'
```

## Sample Response
```shell
{
    "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
    "criteria": [
        {
            "id": "d224d7d6-8673-4b26-86cc-47e2b471799b",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "name": "cost",
            "rank": 1,
            "score": 0.33333334
        },
        {
            "id": "fd5fc079-c9d0-45fa-8acf-b8c03bd52318",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "name": "reliability",
            "rank": 2,
            "score": 0.16666667
        }
    ],
    "options": [
        {
            "id": "7a1762bc-036c-439e-8006-345bad250d43",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "name": "aws",
            "totalScore": 0.11111112
        },
        {
            "id": "5a93d77f-ebeb-404f-8a3d-793a5bf5cfa2",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "name": "azure",
            "totalScore": 0.1388889
        }
    ],
    "optionsCriterion": [
        {
            "id": "b6ba7fb4-80f9-426a-9c6b-fbe0d17281df",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "optionsId": "5a93d77f-ebeb-404f-8a3d-793a5bf5cfa2",
            "criterionId": "d224d7d6-8673-4b26-86cc-47e2b471799b",
            "rank": 1,
            "score": 0.33333334
        },
        {
            "id": "978a29d3-7d83-40ff-a4d5-0a50e8b080cb",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "optionsId": "7a1762bc-036c-439e-8006-345bad250d43",
            "criterionId": "d224d7d6-8673-4b26-86cc-47e2b471799b",
            "rank": 2,
            "score": 0.16666667
        },
        {
            "id": "f5796bc0-db38-4578-96e7-04ef49f35bb2",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "optionsId": "7a1762bc-036c-439e-8006-345bad250d43",
            "criterionId": "fd5fc079-c9d0-45fa-8acf-b8c03bd52318",
            "rank": 1,
            "score": 0.33333334
        },
        {
            "id": "824c5715-753d-4e2e-ac80-38a9fe6d26d8",
            "analysisId": "dcafc22f-6b8c-4bf9-bed7-c6f173a46b77",
            "optionsId": "5a93d77f-ebeb-404f-8a3d-793a5bf5cfa2",
            "criterionId": "fd5fc079-c9d0-45fa-8acf-b8c03bd52318",
            "rank": 2,
            "score": 0.16666667
        }
    ]
}
```

Things to note:
* The winning option will have the highest `options.totalScore`

# Future work for Future Hack-a-thons
* Harding
  * Add validation to POST request
  * Additional testing in Spock
* Move Database to PostGres
* Host in the OpsToolkit
* Create a React Frontend
* Rewrite Service in Kotlin/Micronaut
