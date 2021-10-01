## Methodology Used and Rationale

# Background

Since I am new to the Kotlin programming language and the Exposed framework used in the Antaeus project, I had to spend quite some time looking up a number of terms and perusing the documentation for both platforms. Thereafter, I also spent some time trying to understand the Antaeus code organization, and its core functionality.

Once I had a basic familiarity with the project, I decided to use a [Design by Contract](https://en.wikipedia.org/wiki/Design_by_contract) approach, which means the core classes in the project where the main logic is supposed to reside, namely the PaymentProvider and the BillingService, have to perform certain steps in a particular order, in a similar fashion to a business contract. Here are the steps to be performed by the PaymentProvider:

# @PaymentProvider

**Steps**:

* Validate a customer (if he/she exists)

* Validate an invoice (if the invoice status is pending, which means we can proceed to process it)

* Validate the invoice currency (if it matches what the customer has specified as his/her default currency)

* Save the charge details (A Charge Detail is a record of the payment that has been made, showing the payment method used and is important for financial reconciliation)

* Deduct the customer balance (from the comments/documentation contained in the PaymentProvider, each customer has an account containing their available funds, and this balance should reflect in the system)

* Make an HTTP request with the payment information to the provider - Also in the comments/documentation contained in the PaymentProvider, a network exception should be thrown in the event of a network error. This usually happens when an HTTP request to a provider fails.

* Update invoice status to paid - If all the above steps have been successful, the invoice can then be updated to paid

* Return appropriate errors/exceptions for all the above steps - the errors and exceptions to be returned were specified in the comments/documentation in the PaymentProvider


Here are the steps to be carried out by the Billing service:

# @BillingService

**Steps**

* Run cronjob at the beginning of the month that calls the PaymentProvider interface to charge an invoice. The cronjob has the following sub-tasks:
    * Fetch all pending invoices
    * Call the charge method (in the PaymentProvider) to charge an invoice.
    
    
The following classes/interfaces were created to support the above two:


# Additional Services Created

* `ChargeDetailsService` - assists in saving a charge detail

* `CurrencyService`- validates the invoice currency

* `CustomerAccountService` - updates the customer balance

* `InvoiceCronJob` - executes the monthly cronjob for charging an invoice


The following new models were created to support the above services

# Additional Models Created

* `ChargeDetails`

* `CustomerAccount`
    
    
# Additional Libraries Added

* [Joda Time](https://www.joda.org/joda-time/) - For date/time manipulation

* [Quartz Scheduler](https://www.quartz-scheduler.org/) - For cronjob creation


# Additional Unit Tests Created

* `ChargeDetailsServiceTest`
* `CurrencyServiceTest`
* `CustomerAccountServiceTest`

**NB:** I also added a new spec to the `InvoiceServiceTest`


# Time Taken

**Planning and Design:** 2 working days

**Development:** 8 working days

**Testing (automated and maual):** 1 working day

**Total:** 11 working days








## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

## Developing

Requirements:
- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file and it is going to setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!
