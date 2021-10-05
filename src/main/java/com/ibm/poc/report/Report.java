
/**
 * Copyright 2020 TRINITY MOBILITY PVT LTD. All rights reserved and Use is subject to license terms.
 *
 * File Name  : Report.java
 * Type       : Java Class
 * Product    : trinityICCC
 * Version    : 2.0
 */
package com.ibm.poc.report;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Shreyas
 */
@Getter
@Setter
// @JsonIgnoreProperties(ignoreUnknown = true)
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString
public class Report {
  private String title;
  private String name;
  private List<Parameter> parameters;

  public Report(String title, String name) {
    this.title = title;
    this.name = name;
  }

  @Getter
  @Setter
  public static class Parameter {

    private String title;
    private String name;
    private ParameterType type;

    public Parameter(String promptText, String name2, ParameterType parameterType) {
      this.title = promptText;
      this.name = name2;
      this.type = parameterType;
    }

  }

  public enum ParameterType {
    INT, STRING
  }

}
