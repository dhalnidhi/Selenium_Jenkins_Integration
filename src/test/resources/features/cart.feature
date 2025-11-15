@cart_page @smoke
Feature: Cart page
  This file contains all the testcases related to Cart page


  @validateTotalAmount @smoke
  Scenario: Verify Total amount should match quantity multiply by price
    Given I am logged into application with user "nidhi"
    Then I add first item to the cart
    Then check Price and Quantity in Cart page for "Blue Top"
    Then validate the total amount
