Feature: Operations about user
  Scenario: add user OK
    Given The user data is read from "user_OK.json"
    When I send a post request to "/user/createWithList"
    Then the response status should be 200
    And The response body matches the reference from "Ref_user_OK.json"
    
  Scenario: add user KO
    Given The user data is read from "user_KO.json"
    When I send a post request to "/user/createWithList"
    Then the response status should be 500
    And The response body matches the reference from "Ref_user_KO.json"