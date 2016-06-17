Feature: Localization

  @C77945 @regression
  Scenario Outline: Verify registration screen has German-localized strings
    Given I switch to Sign In page
    When I switch language to <Language>
    And I switch to registration page
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
    Given I switch to Sign In page
    Given I switch language to <Language>
    Given I switch to Sign In page
    Then I see a string <LoginLink> on the page
    And I see a placeholder <EmailPlaceholder> on the page
    And I see a placeholder <PasswordPlaceholder> on the page
    And I see a string <RememberMeText> on the page
    And I see a button with <SignInButton> on the page
    And I see a string <ForgotPasswordLink> on the page

    Examples:
      | Language | LoginLink | EmailPlaceholder | PasswordPlaceholder | RememberMeText     | SignInButton | ForgotPasswordLink |
      | de       | LOGIN     | E-Mail-Adresse   | Passwort            | ANGEMELDET BLEIBEN | Login        | Passwort vergessen |

  @C131208 @regression
  Scenario Outline: Verify conversation view and list has German-localized strings
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I switch language to <Language>
    And I see Contact list with name <Contact>
    And I see a string <ConversationViewText> on the page
    And I see a string <ContactListText> on the page
    When I open People Picker from Contact List
    Then I see a placeholder <SearchPlaceHolder> on the page
    When I close People Picker
    And I click on options button for conversation <Contact>
    Then I see a conversation option <ConvOption1> on the page
    And I see a conversation option <ConvOption2> on the page
    And I see a conversation option <ConvOption3> on the page
    And I see a conversation option <ConvOption4> on the page

    Examples:
      | Login      | Password      | Name      | Contact   | Language | ConversationViewText | ContactListText  | SearchPlaceHolder                | ConvOption1   | ConvOption2 | ConvOption3 | ConvOption4 |
      | user1Email | user1Password | user1Name | user2Name | de       | HINZUGEFÜGT          | KONTAKTE         | Namen oder E-Mail-Adresse suchen | Stummschalten | Archivieren | Löschen     | Blockieren  |

  @C136458 @regression
  Scenario Outline: Verify support pages are opened in correct language (<Language>)
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I switch language to <Language>
    Then I see People Picker
    And I close People Picker
    Then I open self profile
    And I click gear button on self profile page
    And I select <SupportButton> menu item on self profile page
    And I switch to support page tab
    Then I see a title <PageTitle> on the page
    And I see a placeholder <SearchFieldPlaceholder> on the page
    And I see localized <Language> support page

    Examples:
      | Login      | Password      | Name      | Language | SupportButton | PageTitle      | SearchFieldPlaceholder |
      | user1Email | user1Password | user1Name | de       | Hilfe         | Wire Hilfe     | Gib ein Schlagwort ein |
      | user1Email | user1Password | user1Name | en       | Support       | Wire – Support | Enter a keyword        |