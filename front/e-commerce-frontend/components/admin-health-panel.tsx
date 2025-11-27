"use client"
import React, { useEffect, useState, useRef } from "react"

type HealthTarget = {
  target: string
  healthy: boolean
  detail: string
  rtt_ms: number
  timestamp: number
}

const REFRESH_INTERVAL = 10000 

export default function AdminHealthPanel() {
  const [targets, setTargets] = useState<HealthTarget[]>([])
  const [error, setError] = useState<string | null>(null)
  const [progress, setProgress] = useState(0)
  const progressRef = useRef<number>(0)

  useEffect(() => {
    async function fetchHealth() {
      setError(null)
      try {
        const res = await fetch("http://localhost:8088/health")
        if (!res.ok) throw new Error("Failed to fetch health")
        const data = await res.json()
        setTargets(data.targets)
      } catch (err: any) {
        setError(err.message)
      }
    }
    fetchHealth()
    const interval = setInterval(() => {
      fetchHealth()
      progressRef.current = 0
      setProgress(0)
    }, REFRESH_INTERVAL)

    const progressInterval = setInterval(() => {
      progressRef.current += 100
      setProgress(progressRef.current)
      if (progressRef.current >= REFRESH_INTERVAL) progressRef.current = REFRESH_INTERVAL
    }, 100)

    return () => {
      clearInterval(interval)
      clearInterval(progressInterval)
    }
  }, [])

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">gRPC Health Panel</h2>
      <div className="w-full h-2 bg-gray-200 rounded mb-4 overflow-hidden">
        <div
          className="h-2 bg-blue-500 transition-all"
          style={{ width: `${(progress / REFRESH_INTERVAL) * 100}%` }}
        />
      </div>
      {error && <div className="text-red-500 mb-2">Error: {error}</div>}
      <table className="min-w-full border">
        <thead>
          <tr>
            <th className="border px-2 py-1">Target</th>
            <th className="border px-2 py-1">Healthy</th>
            <th className="border px-2 py-1">RTT (ms)</th>
            <th className="border px-2 py-1">Detail</th>
          </tr>
        </thead>
        <tbody>
          {targets.length === 0 ? (
            <tr>
              <td colSpan={4} className="text-center py-4 text-gray-400">No data yet...</td>
            </tr>
          ) : (
            targets.map(t => (
              <tr key={t.target}>
                <td className="border px-2 py-1">{t.target}</td>
                <td className={`border px-2 py-1 ${t.healthy ? "text-green-600" : "text-red-600"}`}>
                  {t.healthy ? "UP" : "DOWN"}
                </td>
                <td className="border px-2 py-1">{t.rtt_ms}</td>
                <td className="border px-2 py-1 text-xs">{t.detail}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}