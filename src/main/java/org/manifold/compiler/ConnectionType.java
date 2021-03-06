package org.manifold.compiler;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class ConnectionType extends TypeValue {
  private final ImmutableMap<String, TypeValue> attributes;
  
  public ConnectionType(Map<String, TypeValue> attributes){
    this.attributes = ImmutableMap.copyOf(attributes);
  }
  
  public ImmutableMap<String, TypeValue> getAttributes() {
    return attributes;
  }
  
  @Override
  public void accept(ValueVisitor visitor) {
    visitor.visit(this);
  }
  
}
