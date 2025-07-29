import { useState } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, LineChart, Line } from "recharts";
import "../css/ChartCard.css";
const ChartCard = ({ activitySummary, activityTimeline }) => {
  const [activeTab, setActiveTab] = useState("type");

  return (
    <div className="chart-card">
      {/* Tabs */}
      <div className="chart-tabs">
        <button
          className={`chart-tab ${activeTab === "type" ? "active" : ""}`}
          onClick={() => setActiveTab("type")}
        >
          By Type
        </button>
        <button
          className={`chart-tab ${activeTab === "date" ? "active" : ""}`}
          onClick={() => setActiveTab("date")}
        >
          By Date
        </button>
      </div>

      {/* Chart */}
      <div className="chart-container">
        {activeTab === "type" ? (
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={activitySummary} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis allowDecimals={false} />
              <Tooltip />
              <Bar dataKey="count" fill="#3b82f6" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        ) : (
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={activityTimeline} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis allowDecimals={false} />
              <Tooltip />
              <Line type="monotone" dataKey="count" stroke="#10b981" strokeWidth={3} dot={{ r: 4 }} />
            </LineChart>
          </ResponsiveContainer>
        )}
      </div>
    </div>
  );
};

export default ChartCard;
