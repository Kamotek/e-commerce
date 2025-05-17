import type { Metadata } from "next"
import AdminAnalytics from "@/components/admin-analytics"

export const metadata: Metadata = {
  title: "Analytics - TechHub Admin",
  description: "View site analytics and metrics",
}

export default function AdminAnalyticsPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Analytics Dashboard</h1>
      <AdminAnalytics />
    </div>
  )
}
