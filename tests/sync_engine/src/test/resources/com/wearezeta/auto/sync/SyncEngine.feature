Feature: Sync Engine

  Scenario Outline: Sync Engine smoke test
    Given I start all platform clients
    And I sign in to all platform clients and go to conversation <ConversationName>
    When I run serial sync engine test
    Then I collect messages order data
    And I collect builds and devices info
    And I build results report
    And I perform acceptance checks

    Examples: 
      | ConversationName |
      | SyncEngineTest   |
