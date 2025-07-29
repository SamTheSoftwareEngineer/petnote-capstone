import { useEffect, useState } from "react";
import ChartCard from "../components/ChartCard";
import "../css/Stats.css";
const Stats = ({ user }) => {
  const [petRows, setPetRows] = useState([]);
  const [activitySummary, setActivitySummary] = useState([]);
  const [activityTimeline, setActivityTimeline] = useState([]);
  const [selectedPet, setSelectedPet] = useState("all");
  const [selectedType, setSelectedType] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetch(`http://localhost:8080/api/activities/user/${user.id}/pets-with-activities`)
      .then((res) => res.json())
      .then((data) => {
        setPetRows(data);

        let allActivities = data.flatMap((row) =>
          row.activities.map((activity) => ({
            ...activity,
            petName: row.pet.petName,
          }))
        );

        if (selectedPet !== "all") {
          allActivities = allActivities.filter((a) => a.petName === selectedPet);
        }

        if (selectedType !== "all") {
          allActivities = allActivities.filter((a) => a.activityName === selectedType);
        }

        // Build summary data
        const summaryMap = allActivities.reduce((acc, activity) => {
          acc[activity.activityName] = (acc[activity.activityName] || 0) + 1;
          return acc;
        }, {});
        setActivitySummary(
          Object.entries(summaryMap).map(([name, count]) => ({ name, count }))
        );

        // Build timeline data
        const timelineMap = allActivities.reduce((acc, activity) => {
          const date = new Date(activity.activityDate).toISOString().split("T")[0];
          acc[date] = (acc[date] || 0) + 1;
          return acc;
        }, {});
        setActivityTimeline(
          Object.entries(timelineMap)
            .map(([date, count]) => ({ date, count }))
            .sort((a, b) => new Date(a.date) - new Date(b.date))
        );
      })
      .catch((err) => console.error("Failed to load stats:", err))
      .finally(() => setLoading(false));
  }, [user.id, selectedPet, selectedType]);

  if (loading) return <p>Loading stats...</p>;

  return (
    <div className="stats-page">
      <h1>Pet Activity Stats</h1>

      {/* Filters */}
      <div className="chart-filters">
        <label>
          Pet:
          <select value={selectedPet} onChange={(e) => setSelectedPet(e.target.value)}>
            <option value="all">All Pets</option>
            {petRows.map(({ pet }) => (
              <option key={pet.id} value={pet.petName}>
                {pet.petName}
              </option>
            ))}
          </select>
        </label>

        <label>
          Activity Type:
          <select value={selectedType} onChange={(e) => setSelectedType(e.target.value)}>
            <option value="all">All Activities</option>
            {[...new Set(petRows.flatMap(({ activities }) => activities.map((a) => a.activityName)))]
              .map((type, idx) => (
                <option key={idx} value={type}>
                  {type}
                </option>
              ))}
          </select>
        </label>
      </div>

      {/* Charts */}
      <ChartCard activitySummary={activitySummary} activityTimeline={activityTimeline} />
    </div>
  );
};

export default Stats;
