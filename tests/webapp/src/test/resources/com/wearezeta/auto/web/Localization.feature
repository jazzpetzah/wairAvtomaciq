Feature: Localization

  @C77945 @regression
  Scenario Outline: Verify registration screen has German-localized strings
    When I switch language to <Language>
    Then I see Registration page
    And I see a string <CreateAccountLink> on the page
    And I see a placeholder <NamePlaceholder> on the page
    And I see a placeholder <EmailPlaceholder> on the page
    And I see a placeholder <PasswordPlaceholder> on the page
    And I see a button with <RegisterButton> on the page
    And I see a string <ToCText> on the page

    Examples:
      | Language | CreateAccountLink | NamePlaceholder | EmailPlaceholder | PasswordPlaceholder          | RegisterButton | ToCText                                |
      | de       | KONTO ERSTELLEN   | Name            | E-Mail-Adresse   | Passwort (min. acht Zeichen) | Erstellen      | Ich akzeptiere die Nutzungsbedingungen |

  @C77947 @regression
  Scenario Outline: Verify login screen has German-localized strings
    When I switch language to <Language>
    Then I see Registration page
    And I switch to Sign In page
    Then I see a string <LoginLink> on the page
    And I see a placeholder <EmailPlaceholder> on the page
    And I see a placeholder <PasswordPlaceholder> on the page
    And I see a string <RememberMeText> on the page
    And I see a button with <SignInButton> on the page
    And I see a string <ForgotPasswordLink> on the page

    Examples:
      | Language | LoginLink | EmailPlaceholder | PasswordPlaceholder | RememberMeText     | SignInButton | ForgotPasswordLink |
      | de       | LOGIN     | E-Mail-Adresse   | Passwort            | ANGEMELDET BLEIBEN | Login        | Passwort vergessen |

  @C131208 @staging
  Scenario Outline: Verify conversation view and list has German-localized strings
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I switch language to <Language>
    And I see Contact list with name <Contact>
    And I see a string HINZUGEFÜGT on the page
    And I see a string <ContactListPlaceholder> on the page
    When I open People Picker from Contact List
    Then I see a placeholder <SearchPlaceHolder> on the page
    When I close People Picker
    And I click on options button for conversation <Contact>
    Then I see a conversation option Stummschalten on the page
    And I see a conversation option Archivieren on the page
    And I see a conversation option Löschen on the page
    And I see a conversation option Blockieren on the page

    Examples:
      | Login      | Password      | Name      | Contact   | Language | ContactListPlaceholder | SearchPlaceHolder                |
      | user1Email | user1Password | user1Name | user2Name | de       | UNTERHALTUNG BEGINNEN  | Namen oder E-Mail-Adresse suchen |