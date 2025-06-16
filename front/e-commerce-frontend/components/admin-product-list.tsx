"use client"

import { useState, useEffect, useCallback } from "react"
import { Product, Page, fetchAllProductsAdmin, createProduct, updateProduct, deleteProduct } from "@/lib/fetch-products"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Pencil, Trash, MoreHorizontal, Plus, Search, Eye, Loader2, AlertCircle } from "lucide-react"

// Stan początkowy dla nowego produktu, zgodny z modelem
const INITIAL_PRODUCT_STATE: Omit<Product, 'id' | 'status' | 'rating' | 'reviewCount'> = {
  name: "",
  description: "",
  price: 0,
  originalPrice: undefined,
  category: "",
  inventory: 0,
  imageUrls: [],
  specifications: {},
  brand: "",
  badge: "",
};


export default function AdminProductList() {
  const [productsPage, setProductsPage] = useState<Page<Product> | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize] = useState(10);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const [currentProduct, setCurrentProduct] = useState<Partial<Product> | null>(null);
  const [modalMode, setModalMode] = useState<'add' | 'edit'>('add');

  const fetchProducts = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await fetchAllProductsAdmin(currentPage, pageSize);
      setProductsPage(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "An unknown error occurred");
    } finally {
      setIsLoading(false);
    }
  }, [currentPage, pageSize]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const openAddModal = () => {
    setModalMode('add');
    setCurrentProduct(INITIAL_PRODUCT_STATE);
    setIsModalOpen(true);
  };

  const openEditModal = (product: Product) => {
    setModalMode('edit');
    setCurrentProduct(product);
    setIsModalOpen(true);
  };

  const openDeleteModal = (product: Product) => {
    setCurrentProduct(product);
    setIsDeleteModalOpen(true);
  }

  const handleFormSubmit = async () => {
    if (!currentProduct) return;

    try {
      if (modalMode === 'add') {
        await createProduct(currentProduct as Omit<Product, 'id'>);
      } else if (currentProduct.id) {
        await updateProduct(currentProduct.id, currentProduct);
      }
      setIsModalOpen(false);
      fetchProducts();
    } catch (e) {
      alert(`Error: ${e instanceof Error ? e.message : 'Unknown error'}`);
    }
  };

  const handleDelete = async () => {
    if (!currentProduct || !currentProduct.id) return;

    try {
      await deleteProduct(currentProduct.id);
      setIsDeleteModalOpen(false);
      fetchProducts(); // Odśwież listę
    } catch (e) {
      alert(`Error: ${e instanceof Error ? e.message : 'Unknown error'}`);
    }
  }

  const handleInputChange = (field: keyof Product, value: any) => {
    setCurrentProduct(prev => prev ? { ...prev, [field]: value } : null);
  }

  return (
      <div className="space-y-4">
        <div className="flex justify-between items-center">
          <h2 className="text-2xl font-semibold">Product Catalog</h2>
          <Button onClick={openAddModal}>
            <Plus className="mr-2 h-4 w-4" />
            Add Product
          </Button>
        </div>

        {isLoading && <div className="flex justify-center items-center py-8"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>}
        {error && <div className="text-red-600 bg-red-100 p-4 rounded-md flex items-center"><AlertCircle className="mr-2"/> {error}</div>}

        {!isLoading && !error && productsPage && (
            <>
              <div className="rounded-md border">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Name</TableHead>
                      <TableHead>Category</TableHead>
                      <TableHead>Brand</TableHead>
                      <TableHead>Price</TableHead>
                      <TableHead>Inventory</TableHead>
                      <TableHead>Status</TableHead>
                      <TableHead className="w-[100px]">Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {productsPage.content.length === 0 ? (
                        <TableRow>
                          <TableCell colSpan={7} className="text-center py-8">No products found.</TableCell>
                        </TableRow>
                    ) : (
                        productsPage.content.map((product) => (
                            <TableRow key={product.id}>
                              <TableCell className="font-medium">{product.name}</TableCell>
                              <TableCell>{product.category}</TableCell>
                              <TableCell>{product.brand}</TableCell>
                              <TableCell>${product.price.toFixed(2)}</TableCell>
                              <TableCell>{product.inventory}</TableCell>
                              <TableCell>
                        <span
                            className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
                                product.status === "In Stock"
                                    ? "bg-green-100 text-green-800"
                                    : product.status === "Low Stock"
                                        ? "bg-yellow-100 text-yellow-800"
                                        : "bg-red-100 text-red-800"
                            }`}
                        >
                            {product.status}
                        </span>
                              </TableCell>
                              <TableCell>
                                <DropdownMenu>
                                  <DropdownMenuTrigger asChild>
                                    <Button variant="ghost" size="icon"><MoreHorizontal className="h-4 w-4" /></Button>
                                  </DropdownMenuTrigger>
                                  <DropdownMenuContent align="end">
                                    <DropdownMenuLabel>Actions</DropdownMenuLabel>
                                    <DropdownMenuItem onClick={() => window.open(`/catalog/product/${product.id}`, "_blank")}><Eye className="mr-2 h-4 w-4" />View</DropdownMenuItem>
                                    <DropdownMenuItem onClick={() => openEditModal(product)}><Pencil className="mr-2 h-4 w-4" />Edit</DropdownMenuItem>
                                    <DropdownMenuSeparator />
                                    <DropdownMenuItem className="text-red-600" onClick={() => openDeleteModal(product)}><Trash className="mr-2 h-4 w-4" />Delete</DropdownMenuItem>
                                  </DropdownMenuContent>
                                </DropdownMenu>
                              </TableCell>
                            </TableRow>
                        ))
                    )}
                  </TableBody>
                </Table>
              </div>
              <div className="flex justify-center items-center space-x-2">
                <Button variant="outline" onClick={() => setCurrentPage(p => p - 1)} disabled={currentPage === 1}>Previous</Button>
                <span>Page {productsPage.number + 1} of {productsPage.totalPages}</span>
                <Button variant="outline" onClick={() => setCurrentPage(p => p + 1)} disabled={currentPage === productsPage.totalPages}>Next</Button>
              </div>
            </>
        )}

        {/* Add/Edit Product Dialog */}
        <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
          <DialogContent className="sm:max-w-[625px]">
            <DialogHeader>
              <DialogTitle>{modalMode === 'add' ? 'Add New Product' : 'Edit Product'}</DialogTitle>
              <DialogDescription>
                {modalMode === 'add' ? 'Fill in the details to add a new product.' : 'Make changes to the product details.'}
              </DialogDescription>
            </DialogHeader>
            {currentProduct && (
                <div className="grid gap-4 py-4 max-h-[70vh] overflow-y-auto pr-6">
                  <div className="space-y-2">
                    <Label htmlFor="name">Product Name</Label>
                    <Input id="name" value={currentProduct.name || ''} onChange={(e) => handleInputChange('name', e.target.value)} />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="description">Description</Label>
                    <Textarea id="description" value={currentProduct.description || ''} onChange={(e) => handleInputChange('description', e.target.value)} />
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="category">Category</Label>
                      <Input id="category" value={currentProduct.category || ''} onChange={(e) => handleInputChange('category', e.target.value)} />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="brand">Brand</Label>
                      <Input id="brand" value={currentProduct.brand || ''} onChange={(e) => handleInputChange('brand', e.target.value)} />
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="price">Price ($)</Label>
                      <Input id="price" type="number" value={currentProduct.price || 0} onChange={(e) => handleInputChange('price', parseFloat(e.target.value))} />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="originalPrice">Original Price ($)</Label>
                      <Input id="originalPrice" type="number" value={currentProduct.originalPrice || ''} onChange={(e) => handleInputChange('originalPrice', parseFloat(e.target.value))} />
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="inventory">Inventory</Label>
                      <Input id="inventory" type="number" value={currentProduct.inventory || 0} onChange={(e) => handleInputChange('inventory', parseInt(e.target.value, 10))} />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="badge">Badge (e.g., 'New', 'Sale')</Label>
                      <Input id="badge" value={currentProduct.badge || ''} onChange={(e) => handleInputChange('badge', e.target.value)} />
                    </div>
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="imageUrls">Image URLs (comma-separated)</Label>
                    <Textarea id="imageUrls" value={Array.isArray(currentProduct.imageUrls) ? currentProduct.imageUrls.join(', ') : ''} onChange={(e) => handleInputChange('imageUrls', e.target.value.split(',').map(url => url.trim()))} />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="specifications">Specifications (JSON format)</Label>
                    <Textarea id="specifications" rows={4} value={currentProduct.specifications ? JSON.stringify(currentProduct.specifications, null, 2) : '{}'} onChange={(e) => {
                      try { handleInputChange('specifications', JSON.parse(e.target.value)) } catch (err) { /* ignore invalid json while typing */ }
                    }}/>
                  </div>
                </div>
            )}
            <DialogFooter>
              <Button variant="outline" onClick={() => setIsModalOpen(false)}>Cancel</Button>
              <Button onClick={handleFormSubmit}>Save</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <Dialog open={isDeleteModalOpen} onOpenChange={setIsDeleteModalOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Confirm Deletion</DialogTitle>
              <DialogDescription>
                Are you sure you want to delete this product? This action cannot be undone.
              </DialogDescription>
            </DialogHeader>
            {currentProduct && (
                <div className="py-4 font-medium">{currentProduct.name}</div>
            )}
            <DialogFooter>
              <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>Cancel</Button>
              <Button variant="destructive" onClick={handleDelete}>Delete</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>
  )
}