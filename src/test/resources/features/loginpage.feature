@login_page
Feature: Login page
  This file contains all the testcases related to login page


  @loginAsUser @smoke
  Scenario: Login to AutomationExercise Application
    Given I am logged into application with user "nidhi"
    Then I should be logged in successfully.

