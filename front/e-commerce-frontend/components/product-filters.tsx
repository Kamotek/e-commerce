"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Slider } from "@/components/ui/slider"
import { Checkbox } from "@/components/ui/checkbox"
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { ActiveFilters } from "@/app/catalog/page";

export interface FilterState {
  priceRange: [number, number];
  selectedCategories: string[];
  selectedBrands: string[];
  inStock: boolean;
  onSale: boolean;
  sortBy: string;
}

interface ProductFiltersProps {
  onApplyFilters: (filters: FilterState) => void;
  defaultValues: ActiveFilters;
}

export default function ProductFilters({ onApplyFilters, defaultValues }: ProductFiltersProps) {
  const [priceRange, setPriceRange] = useState<[number, number]>(defaultValues.priceRange);
  const [selectedCategories, setSelectedCategories] = useState<string[]>(defaultValues.categories);
  const [selectedBrands, setSelectedBrands] = useState<string[]>(defaultValues.brands);
  const [inStock, setInStock] = useState(defaultValues.inStock);
  const [onSale, setOnSale] = useState(false);
  const [sortBy, setSortBy] = useState(defaultValues.sortBy);

  const categories = [
    { id: "laptops", label: "Laptops" },
    { id: "desktops", label: "Desktops" },
    { id: "smartphones", label: "Smartphones" },
    { id: "tablets", label: "Tablets" },
    { id: "monitors", label: "Monitors" },
    { id: "keyboards", label: "Keyboards" },
    { id: "mice", label: "Mice" },
    { id: "headphones", label: "Headphones" },
    { id: "cpus", label: "CPUs" },
    { id: "gpus", label: "GPUs" },
    { id: "motherboards", label: "Motherboards" },
    { id: "ram", label: "RAM" },
    { id: "storage", label: "Storage" },
  ]

  const brands = [
    { id: "apple", label: "Apple" },
    { id: "samsung", label: "Samsung" },
    { id: "dell", label: "Dell" },
    { id: "hp", label: "HP" },
    { id: "lenovo", label: "Lenovo" },
    { id: "asus", label: "Asus" },
    { id: "acer", label: "Acer" },
    { id: "msi", label: "MSI" },
    { id: "intel", label: "Intel" },
    { id: "amd", label: "AMD" },
    { id: "nvidia", label: "NVIDIA" },
  ]

  const handleCategoryChange = (categoryId: string, checked: boolean) => {
    setSelectedCategories(prev =>
        checked ? [...prev, categoryId] : prev.filter(id => id !== categoryId)
    );
  };

  const handleBrandChange = (brandId: string, checked: boolean) => {
    setSelectedBrands(prev =>
        checked ? [...prev, brandId] : prev.filter(id => id !== brandId)
    );
  };

  const handleApply = () => {
    onApplyFilters({
      priceRange,
      selectedCategories,
      selectedBrands,
      inStock,
      onSale,
      sortBy,
    });
  };

  const handleSortChange = (newSortValue: string) => {
    setSortBy(newSortValue);

    onApplyFilters({
      priceRange,
      selectedCategories,
      selectedBrands,
      inStock,
      onSale,
      sortBy: newSortValue,
    });
  };

  const handleReset = () => {
    setPriceRange(defaultValues.priceRange);
    setSelectedCategories(defaultValues.categories);
    setSelectedBrands(defaultValues.brands);
    setInStock(defaultValues.inStock);
    setOnSale(false);
    setSortBy(defaultValues.sortBy);

    onApplyFilters({
      priceRange: defaultValues.priceRange,
      selectedCategories: defaultValues.categories,
      selectedBrands: defaultValues.brands,
      inStock: defaultValues.inStock,
      onSale: false,
      sortBy: defaultValues.sortBy
    });
  }

  return (
      <div className="space-y-6">
        <div>
          <h3 className="text-lg font-medium mb-4">Filters</h3>
          <Select value={sortBy} onValueChange={handleSortChange}>
            <SelectTrigger>
              <SelectValue placeholder="Sort by" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="featured">Featured</SelectItem>
              <SelectItem value="price-low">Price: Low to High</SelectItem>
              <SelectItem value="price-high">Price: High to Low</SelectItem>
              <SelectItem value="newest">Newest</SelectItem>
              <SelectItem value="rating">Highest Rated</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <Accordion type="multiple" defaultValue={["price", "categories"]}>
          <AccordionItem value="price">
            <AccordionTrigger>Price Range</AccordionTrigger>
            <AccordionContent>
              <div className="space-y-4">
                <Slider defaultValue={priceRange} max={2000} step={10} value={priceRange} onValueChange={(value) => setPriceRange(value as [number, number])} />
                <div className="flex justify-between text-sm">
                  <Input
                      type="number"
                      value={priceRange[0]}
                      onChange={(e) => setPriceRange([+e.target.value, priceRange[1]])}
                      className="w-24"
                  />
                  <Input
                      type="number"
                      value={priceRange[1]}
                      onChange={(e) => setPriceRange([priceRange[0], +e.target.value])}
                      className="w-24"
                  />
                </div>
              </div>
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="categories">
            <AccordionTrigger>Categories</AccordionTrigger>
            <AccordionContent>
              <div className="space-y-2">
                {categories.map((category) => (
                    <div key={category.id} className="flex items-center space-x-2">
                      <Checkbox
                          id={`category-${category.id}`}
                          checked={selectedCategories.includes(category.id)}
                          onCheckedChange={(checked) => handleCategoryChange(category.id, !!checked)}
                      />
                      <Label htmlFor={`category-${category.id}`}>{category.label}</Label>
                    </div>
                ))}
              </div>
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="brands">
            <AccordionTrigger>Brands</AccordionTrigger>
            <AccordionContent>
              <div className="space-y-2">
                {brands.map((brand) => (
                    <div key={brand.id} className="flex items-center space-x-2">
                      <Checkbox
                          id={`brand-${brand.id}`}
                          checked={selectedBrands.includes(brand.id)}
                          onCheckedChange={(checked) => handleBrandChange(brand.id, !!checked)}
                      />
                      <Label htmlFor={`brand-${brand.id}`}>{brand.label}</Label>
                    </div>
                ))}
              </div>
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="availability">
            <AccordionTrigger>Availability</AccordionTrigger>
            <AccordionContent>
              <div className="space-y-2">
                <div className="flex items-center space-x-2">
                  <Checkbox id="in-stock" checked={inStock} onCheckedChange={(checked) => setInStock(!!checked)} />
                  <Label htmlFor="in-stock">In Stock</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Checkbox id="on-sale" checked={onSale} onCheckedChange={(checked) => setOnSale(!!checked)} />
                  <Label htmlFor="on-sale">On Sale</Label>
                </div>
              </div>
            </AccordionContent>
          </AccordionItem>
        </Accordion>

        <Button className="w-full" onClick={handleApply}>Apply Filters</Button>
        <Button variant="outline" className="w-full" onClick={handleReset}>
          Reset Filters
        </Button>
      </div>
  )
}