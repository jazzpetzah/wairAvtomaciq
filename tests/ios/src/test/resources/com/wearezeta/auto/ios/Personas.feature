Feature: Personas

  @persona
  Scenario Outline: Persona sends a text message to a persona
    #needs to be done for real device
    Given I open Wire
    Given I see conversations list
    When I tap on contact name <RandomPersona>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    And I navigate back to conversations list
    #needs to be done for real device
    And I put Wire into background

    Examples:
      | RandomPersona |
      | PersonaB      |

  @persona
  Scenario Outline: Persona sends a picture to a persona
    Given I open Wire
    Given I see conversations list
    When I tap on contact name <RandomPersona>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    Then I see 1 photo in the conversation view
    And I navigate back to conversations list
    And I put Wire into background

    Examples:
      | RandomPersona |
      | PersonaB      |

  #think about how to pick up and end the call via the receiver or sender
  @persona
  Scenario Outline: Persona makes a call to a persona
    Given I open Wire
    Given I see conversations list
    When I tap on contact name <RandomPersona>
    And I tap Audio Call button
    And I see Calling overlay
    And I wait for 240 seconds
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay
    And I navigate back to conversations list
    And I put Wire into background

    Examples:
      | RandomPersona |
      | PersonaB      |

  @persona
  Scenario Outline: Persona opens randomly Wire
    Given I open Wire
    Given I see conversations list
    When I tap on contact name <RandomPersona>
    Then I see history in the conversation view
    And I navigate back to conversations list
    And I put Wire into background

    Examples:
      | RandomPersona |
      | PersonaB      |