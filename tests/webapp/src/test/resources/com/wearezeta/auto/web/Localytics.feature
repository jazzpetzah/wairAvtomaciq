Feature: Localytics

  Scenario Outline: Verify 'regFailed:reason=The given e-mail address or phone number is in use.' stats
    Given I take snapshot of <AttrName> attribute count
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page 
    And I enter user email <ExistingEmail> on Registration page 
    And I enter user password <Password> on Registration page 
    And I submit registration form

    Examples: 
      | Login      | Password      | Name      | ExistingEmail                 | AttrName                                                             |
      | user1Email | user1Password | user1Name | mykola.mokhnach@wearezeta.com | regFailed:reason=The given e-mail address or phone number is in use. |

  @torun
  Scenario Outline: Verify 'regAddedPicture:source=fromPhotoLibrary' stats
    Given I take snapshot of <AttrName> attribute count
    Given There is 1 user where <Name> is me without avatar picture
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I choose <PictureName> as my self picture on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog

    Examples: 
      | Login      | Password      | Name      | PictureName               | AttrName                                |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg | regAddedPicture:source=fromPhotoLibrary |

  @torun
  Scenario Outline: Verify count of each attribute is increased
    Then I verify the count of <AttrName> attribute has been increased within 600 seconds

    Examples:
      | AttrName                                                             |
      | regFailed:reason=The given e-mail address or phone number is in use. |
      | regAddedPicture:source=fromPhotoLibrary                              |