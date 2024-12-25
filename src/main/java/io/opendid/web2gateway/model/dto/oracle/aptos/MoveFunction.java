package io.opendid.web2gateway.model.dto.oracle.aptos;

import java.util.List;

public class MoveFunction {

  private String name;

  private String visibility;

  private Boolean isEntry;

  private Boolean isView;

  private List<MoveFunctionGenericTypeParam> genericTypeParams;

  private List<String> params;

  private List<String> _return;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  public Boolean getEntry() {
    return isEntry;
  }

  public void setEntry(Boolean entry) {
    isEntry = entry;
  }

  public Boolean getView() {
    return isView;
  }

  public void setView(Boolean view) {
    isView = view;
  }

  public List<MoveFunctionGenericTypeParam> getGenericTypeParams() {
    return genericTypeParams;
  }

  public void setGenericTypeParams(List<MoveFunctionGenericTypeParam> genericTypeParams) {
    this.genericTypeParams = genericTypeParams;
  }

  public List<String> getParams() {
    return params;
  }

  public void setParams(List<String> params) {
    this.params = params;
  }

  public List<String> get_return() {
    return _return;
  }

  public void set_return(List<String> _return) {
    this._return = _return;
  }
}
