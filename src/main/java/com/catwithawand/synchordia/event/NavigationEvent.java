package com.catwithawand.synchordia.event;

public abstract class NavigationEvent {

  private Object source;

  public NavigationEvent(Object source) {
    this.source = source;
  }

  public Object getSource() {
    return source;
  }

}
