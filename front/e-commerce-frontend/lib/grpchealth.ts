export async function getGrpcHealth() {
    const url = "http://localhost:8088/health";
    const res = await fetch(url, { cache: "no-store" });
    if (!res.ok) throw new Error(`Failed to fetch health (${res.status})`);
    return res.json();
}