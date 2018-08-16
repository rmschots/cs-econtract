package be.cipalschaubroeck.econtract.dto.common;

/**
 * Build information.
 *
 * @author David Debuck
 */
public class BuildInformationDto {

    private String status;

    private String version;

    public BuildInformationDto(final String status, final String version) {
        this.status = status;
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

}
