Feature: Localization

  @C77945 @regression @torun
  Scenario Outline: Verify registration screen has German-localized strings
    When I switch language to <Language>
    Then I see a string <CreateAccountLink> on the page
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