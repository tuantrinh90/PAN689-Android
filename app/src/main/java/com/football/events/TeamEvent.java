package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.TeamResponse;

public class TeamEvent implements IEvent {

    private TeamResponse team;

    public TeamEvent(TeamResponse team) {
        this.team = team;
    }

    public TeamResponse getTeam() {
        return team;
    }
}
