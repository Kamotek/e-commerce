"use client"

import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  AreaChart,
  Area,
} from "@/components/ui/chart"
import { ArrowUpRight, ArrowDownRight } from "lucide-react"

// Mock data for charts
const trafficData = [
  { name: "Jan", value: 4000 },
  { name: "Feb", value: 3000 },
  { name: "Mar", value: 2000 },
  { name: "Apr", value: 2780 },
  { name: "May", value: 1890 },
  { name: "Jun", value: 2390 },
  { name: "Jul", value: 3490 },
]

const conversionData = [
  { name: "Jan", rate: 2.4 },
  { name: "Feb", rate: 2.8 },
  { name: "Mar", rate: 3.2 },
  { name: "Apr", rate: 3.0 },
  { name: "May", rate: 3.5 },
  { name: "Jun", rate: 3.7 },
  { name: "Jul", rate: 4.1 },
]

const deviceData = [
  { name: "Desktop", value: 45 },
  { name: "Mobile", value: 40 },
  { name: "Tablet", value: 15 },
]

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884D8"]

const sourceData = [
  { name: "Direct", value: 30 },
  { name: "Organic Search", value: 25 },
  { name: "Referral", value: 20 },
  { name: "Social Media", value: 15 },
  { name: "Email", value: 10 },
]

const pageViewsData = [
  { name: "Home", views: 12500 },
  { name: "Products", views: 9800 },
  { name: "Cart", views: 4500 },
  { name: "Checkout", views: 3200 },
  { name: "Account", views: 2100 },
]

export default function AdminAnalytics() {
  return (
    <Tabs defaultValue="overview" className="space-y-4">
      <TabsList>
        <TabsTrigger value="overview">Overview</TabsTrigger>
        <TabsTrigger value="traffic">Traffic</TabsTrigger>
        <TabsTrigger value="conversion">Conversion</TabsTrigger>
        <TabsTrigger value="behavior">User Behavior</TabsTrigger>
      </TabsList>

      <TabsContent value="overview" className="space-y-4">
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total Visitors</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">45,231</div>
              <div className="flex items-center text-xs text-green-500">
                <ArrowUpRight className="mr-1 h-4 w-4" />
                <span>+20.1% from last month</span>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Conversion Rate</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">4.1%</div>
              <div className="flex items-center text-xs text-green-500">
                <ArrowUpRight className="mr-1 h-4 w-4" />
                <span>+0.4% from last month</span>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Avg. Session Duration</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">3m 42s</div>
              <div className="flex items-center text-xs text-red-500">
                <ArrowDownRight className="mr-1 h-4 w-4" />
                <span>-12s from last month</span>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Bounce Rate</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">32.4%</div>
              <div className="flex items-center text-xs text-green-500">
                <ArrowDownRight className="mr-1 h-4 w-4" />
                <span>-2.3% from last month</span>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
          <Card className="col-span-4">
            <CardHeader>
              <CardTitle>Website Traffic</CardTitle>
              <CardDescription>Daily visitors over the past month</CardDescription>
            </CardHeader>
            <CardContent className="pl-2">
              <ResponsiveContainer width="100%" height={350}>
                <AreaChart data={trafficData}>
                  <defs>
                    <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
                      <stop offset="95%" stopColor="#8884d8" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Area type="monotone" dataKey="value" stroke="#8884d8" fillOpacity={1} fill="url(#colorValue)" />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <Card className="col-span-3">
            <CardHeader>
              <CardTitle>Traffic Sources</CardTitle>
              <CardDescription>Where your visitors are coming from</CardDescription>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={350}>
                <PieChart>
                  <Pie
                    data={sourceData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {sourceData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </div>
      </TabsContent>

      <TabsContent value="traffic" className="space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>Traffic by Device</CardTitle>
            <CardDescription>Distribution of visitors across different devices</CardDescription>
          </CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={deviceData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {deviceData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="space-y-4">
              <div>
                <h4 className="text-sm font-medium mb-2">Device Breakdown</h4>
                <div className="space-y-4">
                  {deviceData.map((device, index) => (
                    <div key={device.name} className="space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="text-sm font-medium flex items-center">
                          <span
                            className="inline-block w-3 h-3 rounded-full mr-2"
                            style={{ backgroundColor: COLORS[index % COLORS.length] }}
                          />
                          {device.name}
                        </span>
                        <span className="text-sm font-medium">{device.value}%</span>
                      </div>
                      <div className="w-full bg-muted rounded-full h-2">
                        <div
                          className="h-2 rounded-full"
                          style={{
                            width: `${device.value}%`,
                            backgroundColor: COLORS[index % COLORS.length],
                          }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="pt-4">
                <h4 className="text-sm font-medium mb-2">Key Insights</h4>
                <ul className="space-y-2 text-sm">
                  <li>Mobile traffic has increased by 15% compared to last quarter</li>
                  <li>Desktop users have the highest conversion rate at 5.2%</li>
                  <li>Tablet users spend the most time on site with an average of 4m 12s</li>
                </ul>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Top Pages by Views</CardTitle>
            <CardDescription>Most visited pages on your website</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={pageViewsData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" />
                <YAxis dataKey="name" type="category" width={100} />
                <Tooltip />
                <Legend />
                <Bar dataKey="views" fill="#8884d8" name="Page Views" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </TabsContent>

      <TabsContent value="conversion" className="space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>Conversion Rate Trends</CardTitle>
            <CardDescription>Monthly conversion rate over time</CardDescription>
          </CardHeader>
          <CardContent className="pl-2">
            <ResponsiveContainer width="100%" height={350}>
              <LineChart data={conversionData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="rate" stroke="#8884d8" activeDot={{ r: 8 }} name="Conversion Rate (%)" />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <div className="grid gap-4 md:grid-cols-2">
          <Card>
            <CardHeader>
              <CardTitle>Conversion Funnel</CardTitle>
              <CardDescription>Visitor progression through the purchase funnel</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-8">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Product Page Views</span>
                    <span className="text-sm font-medium">45,231</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "100%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Add to Cart</span>
                    <span className="text-sm font-medium">12,456 (27.5%)</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "27.5%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Checkout Started</span>
                    <span className="text-sm font-medium">5,789 (12.8%)</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "12.8%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Purchases Completed</span>
                    <span className="text-sm font-medium">1,856 (4.1%)</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "4.1%" }}></div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Conversion by Traffic Source</CardTitle>
              <CardDescription>Conversion rates across different traffic sources</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Direct</span>
                    <span className="text-sm font-medium">5.2%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "52%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Organic Search</span>
                    <span className="text-sm font-medium">3.8%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "38%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Referral</span>
                    <span className="text-sm font-medium">6.4%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "64%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Social Media</span>
                    <span className="text-sm font-medium">2.1%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "21%" }}></div>
                  </div>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium">Email</span>
                    <span className="text-sm font-medium">7.5%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2.5">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: "75%" }}></div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </TabsContent>

      <TabsContent value="behavior" className="space-y-4">
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          <Card>
            <CardHeader>
              <CardTitle>Average Session Duration</CardTitle>
              <CardDescription>Time spent on site by users</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8">
                <div className="text-5xl font-bold">3:42</div>
                <p className="text-sm text-muted-foreground mt-2">minutes:seconds</p>
                <div className="flex items-center justify-center text-sm text-red-500 mt-4">
                  <ArrowDownRight className="mr-1 h-4 w-4" />
                  <span>-12s from last month</span>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Pages per Session</CardTitle>
              <CardDescription>Average number of pages viewed</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8">
                <div className="text-5xl font-bold">3.8</div>
                <p className="text-sm text-muted-foreground mt-2">pages</p>
                <div className="flex items-center justify-center text-sm text-green-500 mt-4">
                  <ArrowUpRight className="mr-1 h-4 w-4" />
                  <span>+0.3 from last month</span>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Bounce Rate</CardTitle>
              <CardDescription>Percentage of single-page sessions</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8">
                <div className="text-5xl font-bold">32.4%</div>
                <p className="text-sm text-muted-foreground mt-2">of sessions</p>
                <div className="flex items-center justify-center text-sm text-green-500 mt-4">
                  <ArrowDownRight className="mr-1 h-4 w-4" />
                  <span>-2.3% from last month</span>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>User Flow</CardTitle>
            <CardDescription>Common paths users take through your site</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex flex-col items-center">
              <div className="w-full max-w-3xl">
                <div className="flex justify-center mb-4">
                  <div className="bg-primary text-primary-foreground px-4 py-2 rounded-md">Homepage</div>
                </div>

                <div className="flex justify-center">
                  <div className="border-l-2 border-muted-foreground h-8"></div>
                </div>

                <div className="flex justify-between mb-4">
                  <div className="flex flex-col items-center">
                    <div className="border-t-2 border-muted-foreground w-32"></div>
                    <div className="border-l-2 border-muted-foreground h-8"></div>
                    <div className="bg-muted px-4 py-2 rounded-md">Category Page (35%)</div>
                  </div>
                  <div className="flex flex-col items-center">
                    <div className="border-t-2 border-muted-foreground w-32"></div>
                    <div className="border-l-2 border-muted-foreground h-8"></div>
                    <div className="bg-muted px-4 py-2 rounded-md">Search Results (25%)</div>
                  </div>
                  <div className="flex flex-col items-center">
                    <div className="border-t-2 border-muted-foreground w-32"></div>
                    <div className="border-l-2 border-muted-foreground h-8"></div>
                    <div className="bg-muted px-4 py-2 rounded-md">Featured Products (40%)</div>
                  </div>
                </div>

                <div className="flex justify-center">
                  <div className="border-l-2 border-muted-foreground h-8"></div>
                </div>

                <div className="flex justify-center mb-4">
                  <div className="bg-muted px-4 py-2 rounded-md">Product Detail Page (75%)</div>
                </div>

                <div className="flex justify-center">
                  <div className="border-l-2 border-muted-foreground h-8"></div>
                </div>

                <div className="flex justify-between mb-4">
                  <div className="flex flex-col items-center">
                    <div className="border-t-2 border-muted-foreground w-32"></div>
                    <div className="border-l-2 border-muted-foreground h-8"></div>
                    <div className="bg-muted px-4 py-2 rounded-md">Add to Cart (30%)</div>
                  </div>
                  <div className="flex flex-col items-center">
                    <div className="border-t-2 border-muted-foreground w-32"></div>
                    <div className="border-l-2 border-muted-foreground h-8"></div>
                    <div className="bg-muted px-4 py-2 rounded-md">Continue Shopping (45%)</div>
                  </div>
                  <div className="flex flex-col items-center">
                    <div className="border-t-2 border-muted-foreground w-32"></div>
                    <div className="border-l-2 border-muted-foreground h-8"></div>
                    <div className="bg-muted px-4 py-2 rounded-md">Exit (25%)</div>
                  </div>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </TabsContent>
    </Tabs>
  )
}
