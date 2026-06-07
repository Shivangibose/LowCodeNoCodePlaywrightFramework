Feature: SauceDemo login

  Scenario: Login with valid user
    Given user opens url key "urls.inputs.sauceDemo"
    When user enters "${validLogin.inputs.username}" into "usernameInput"
    When user enters "${validLogin.inputs.password}" into "passwordInput"
    When user clicks on "loginButton"
    Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
