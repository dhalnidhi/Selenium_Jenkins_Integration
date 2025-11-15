@smoke
Feature: Home page
  This file contains all the testcases related to home page


  @loginAsUser @smoke
  Scenario: Add to cart - first item
    Given I am logged into application with user "nidhi"
    Then I add first item to the cart
    And the item got added successfully

  @loginAsUser_fail @smoke
  Scenario: Add to cart - first item - failed login
    Given I am logged into application with user "nidhi"
    Then I add first item to the cart
    And the item got added successfully



