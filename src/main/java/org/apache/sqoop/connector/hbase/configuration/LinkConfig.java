/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sqoop.connector.hbase.configuration;

import org.apache.sqoop.model.ConfigClass;
import org.apache.sqoop.model.Input;
import org.apache.sqoop.model.Validator;
import org.apache.sqoop.validation.Status;
import org.apache.sqoop.validation.validators.AbstractValidator;
import org.apache.sqoop.validation.validators.NotEmpty;
import org.apache.sqoop.validation.validators.ClassAvailable;
import org.apache.sqoop.validation.validators.StartsWith;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 */
@ConfigClass(validators = {@Validator(LinkConfig.ConfigValidator.class)})
public class LinkConfig {
  @Input(size = 128, validators = {@Validator(NotEmpty.class), @Validator(ClassAvailable.class)} )
  public String jdbcDriver;

  @Input(size = 128, validators = {@Validator(value = StartsWith.class, strArg = "jdbc:")} )
  public String connectionString;

  @Input(size = 40)
  public String username;

  @Input(size = 40, sensitive = true)
  public String password;

  @Input
  public Map<String, String> jdbcProperties;

  public static class ConfigValidator extends AbstractValidator<LinkConfig> {
    @Override
    public void validate(LinkConfig linkConfig) {
      // See if we can connect to the database
      try {
        DriverManager.getConnection(linkConfig.connectionString, linkConfig.username, linkConfig.password);
      } catch (SQLException e) {
        addMessage(Status.WARNING, "Can't connect to the database with given credentials: " + e.getMessage());
      }
    }
  }
}
