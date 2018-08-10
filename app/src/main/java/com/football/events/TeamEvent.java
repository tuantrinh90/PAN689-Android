package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.TeamResponse;

public class TeamEvent implements IEvent {

    private TeamResponse team;
    private boolean isSetupTeam;

    public TeamEvent(TeamResponse team) {
        this.team = team;
    }

    public TeamEvent(TeamResponse team, boolean isSetupTeam) {
        this.team = team;
        this.isSetupTeam = isSetupTeam;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public boolean isSetupTeam() {
        return isSetupTeam;
    }
}
