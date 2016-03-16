Feature: Localization

  @C77945 @regression
  Scenario Outline: Verify registration screen has German-localized strings
    When I switch language to <Language>
    Then I see a string <CreateAccountLink> on the page
    And I see a placeholder <NamePlaceholder> on the page
    And I see a placeholder <EmailPlaceholder> on the page
    And I see a placeholder <PasswordPlaceholder> on the page
    And I see a button with <SignInButton> on the page
    And I see a string <ToCText> on the page

    Examples:
      | Language | CreateAccountLink | NamePlaceholder | EmailPlaceholder | PasswordPlaceholder          | SignInButton | ToCText                                |
      | de       | KONTO ERSTELLEN   | Name            | E-Mail-Adresse   | Passwort (min. acht Zeichen) | Erstellen    | Ich akzeptiere die Nutzungsbedingungen |