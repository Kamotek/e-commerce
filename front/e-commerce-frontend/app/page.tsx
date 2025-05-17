import { Button } from "@/components/ui/button"
import Link from "next/link"
import { ArrowRight } from "lucide-react"
import FeaturedProducts from "@/components/featured-products"

export default function Home() {
  return (
    <div className="container mx-auto px-4 py-8">
      <section className="py-12 md:py-24">
        <div className="container px-4 md:px-6">
          <div className="grid gap-6 lg:grid-cols-2 lg:gap-12 items-center">
            <div className="space-y-4">
              <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl xl:text-6xl/none">
                Your One-Stop Tech Shop
              </h1>
              <p className="max-w-[600px] text-muted-foreground md:text-xl">
                Discover the latest computers, phones, accessories, and PC parts at competitive prices. Shop with
                confidence with our expert recommendations and top-notch customer service.
              </p>
              <div className="flex flex-col gap-2 min-[400px]:flex-row">
                <Link href="/catalog">
                  <Button size="lg">
                    Browse Catalog
                    <ArrowRight className="ml-2 h-4 w-4" />
                  </Button>
                </Link>
                <Link href="/auth/sign-in">
                  <Button variant="outline" size="lg">
                    Sign In
                  </Button>
                </Link>
              </div>
            </div>
            <img
              src="https://www.makerstations.io/content/images/2022/06/colin-chang-desk-setup-02-2.jpg"
              alt="Latest tech products"
              className="mx-auto aspect-video overflow-hidden rounded-xl object-cover sm:w-full lg:order-last"
            />
          </div>
        </div>
      </section>

      <section className="py-12">
        <div className="container px-4 md:px-6">
          <h2 className="text-2xl font-bold tracking-tight mb-8">Featured Products</h2>
          <FeaturedProducts />
        </div>
      </section>

      <section className="py-12 bg-muted rounded-lg">
        <div className="container px-4 md:px-6">
          <div className="grid gap-6 lg:grid-cols-3">
            <div className="flex flex-col items-center text-center p-4">
              <div className="rounded-full bg-primary p-3 mb-4">
                <svg
                  className="h-6 w-6 text-primary-foreground"
                  fill="none"
                  height="24"
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  viewBox="0 0 24 24"
                  width="24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path d="M5 8a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2z" />
                  <path d="M12 6v12" />
                </svg>
              </div>
              <h3 className="text-xl font-bold">Wide Selection</h3>
              <p className="text-muted-foreground">
                From the latest smartphones to custom PC parts, we have everything you need.
              </p>
            </div>
            <div className="flex flex-col items-center text-center p-4">
              <div className="rounded-full bg-primary p-3 mb-4">
                <svg
                  className="h-6 w-6 text-primary-foreground"
                  fill="none"
                  height="24"
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  viewBox="0 0 24 24"
                  width="24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z" />
                </svg>
              </div>
              <h3 className="text-xl font-bold">Expert Support</h3>
              <p className="text-muted-foreground">Our tech experts are available to help you make the right choice.</p>
            </div>
            <div className="flex flex-col items-center text-center p-4">
              <div className="rounded-full bg-primary p-3 mb-4">
                <svg
                  className="h-6 w-6 text-primary-foreground"
                  fill="none"
                  height="24"
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  viewBox="0 0 24 24"
                  width="24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path d="M9 10V6a3 3 0 0 1 3-3v0a3 3 0 0 1 3 3v4" />
                  <path d="M6 10h12v8a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2z" />
                </svg>
              </div>
              <h3 className="text-xl font-bold">Fast Shipping</h3>
              <p className="text-muted-foreground">
                Get your tech delivered quickly with our expedited shipping options.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  )
}
