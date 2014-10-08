Feature: Sync Engine

  Scenario Outline: Sync Engine smoke test
    Given I start all platform clients
    And I sign in to all platform clients and go to conversation <ConversationName>
#    And I run fast sync engine test
	And I run serial sync engine test
    And I collect messages order data
    Then I build results report
    And I perform acceptance checks

    Examples: 
      | ConversationName |
      | SyncEngineTest   |
