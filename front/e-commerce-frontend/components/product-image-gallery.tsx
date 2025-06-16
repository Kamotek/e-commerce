"use client"

import { useState } from "react"

interface Props {
    imageUrls: string[]
    altText?: string
}

export default function ProductImageGallery({ imageUrls, altText }: Props) {
    const [selected, setSelected] = useState<string | null>(null)

    // Funkcja do zamknięcia modala
    const closeModal = () => setSelected(null)

    return (
        <div>
            {/* Główne zdjęcie (pierwszy element) */}
            <div className="relative w-full aspect-square border rounded-lg overflow-hidden">
                <img
                    src={imageUrls[0] || "/placeholder.svg"}
                    alt={altText || "Zdjęcie produktu"}
                    className="w-full h-full object-contain cursor-pointer"
                    onClick={() => setSelected(imageUrls[0] || "/placeholder.svg")}
                />
            </div>

            {/* Miniaturki poniżej */}
            <div className="grid grid-cols-4 gap-2 mt-2">
                {(imageUrls.length > 0 ? imageUrls : ["/placeholder.svg"]).map((url, idx) => (
                    <div
                        key={idx}
                        className="relative w-full pb-[100%] border rounded-lg overflow-hidden"
                    >
                        <img
                            src={url || "/placeholder.svg"}
                            alt={`${altText || "Zdjęcie produktu"} – widok ${idx + 1}`}
                            className="absolute top-0 left-0 w-full h-full object-cover cursor-pointer"
                            onClick={() => setSelected(url || "/placeholder.svg")}
                        />
                    </div>
                ))}
            </div>

            {/* Modal powiększonego zdjęcia */}
            {selected && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50"
                    onClick={closeModal}
                >
                    <img
                        src={selected}
                        alt="Powiększone zdjęcie"
                        className="max-w-[90%] max-h-[90%] object-contain shadow-lg rounded-lg"
                    />
                </div>
            )}
        </div>
    )
}
