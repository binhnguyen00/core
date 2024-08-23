package net.binhnguyen.module.http.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.binhnguyen.lib.utils.DataSerializer;
import java.util.Objects;

/**
 * @author Bình Nguyễn
 * @Email jackjack2000.kahp@gmail.com
 * @Usage Have more control over what you want to response. <br/>
 *        {@link #component}: @Service name (Service classes) <br/>
 *        {@link #service}: @Component name (Logic classes) <br/>
 *        if [ Status.OK ] then <br/>
 *          {@link #message} should announce 'Success' <br/>
 *          {@link #body} should be return JSON Object (A Employee record for example) <br/>
 *        else <br/>
 *          {@link #message} should announce 'Error' <br/>
 *          {@link #body} should be return Exception message
 */
@NoArgsConstructor @Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerResponse {

  private Status status;
  private String component;
  private String service;
  private String message;

  private long startTimestamp;
  private long finishTimestamp;
  private long executionTimestamp;

  private JsonNode body;

  public ServerResponse(String component, String service) {
    if (component.isEmpty()) this.component = "unknown";
    else this.component = component;
    if (service.isEmpty()) this.service = "unknown";
    else this.service = service;
    this.startTimestamp = System.currentTimeMillis();
  }

  public ServerResponse(Status status, String message) {
    this.status = status;
    this.message = message;
    this.startTimestamp = System.currentTimeMillis();
  }

  public enum Status { OK, ERROR, UNAUTHORIZED }

  public <T> void setBody(T value) {
    if (Objects.isNull(value)) {
      body = null;
    } else {
      body = DataSerializer.JSON.convertObjectToTree(value);
    }
  }

  @JsonIgnore
  public <T> T getBodyAs(Class<T> clazz) {
    if (Objects.isNull(body)) return null;
    return DataSerializer.JSON.convertTreeToObject(this.body, clazz);
  }

  public void setFinishTimestamp(long finishTimestamp) {
    this.finishTimestamp = finishTimestamp;
    this.executionTimestamp = finishTimestamp - startTimestamp;
  }
}