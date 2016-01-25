package com.wearezeta.auto.ios.pages.keyboard;

class KeyboardStateUnknown extends KeyboardState {

    public KeyboardStateUnknown() {
        super(null, null);
    }

    @Override
    public void switchTo(KeyboardState finalState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCharacterSetPattern() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFirstCharacter() {
        throw new UnsupportedOperationException();
    }

}
