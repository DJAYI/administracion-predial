import {
  FileTextIcon,
  RulerIcon,
  UsersIcon,
  DollarSignIcon,
  FolderIcon,
  MapPinIcon,
} from "lucide-react"

export const steps = [
  {
    id: 1,
    title: "Datos Básicos del Predio",
    shortTitle: "Datos Básicos",
    icon: FileTextIcon,
    description: "Identificación y datos generales",
  },
  {
    id: 2,
    title: "Datos Físicos del Predio",
    shortTitle: "Datos Físicos",
    icon: RulerIcon,
    description: "Características y áreas",
  },
  {
    id: 3,
    title: "Titularidad del Predio",
    shortTitle: "Titularidad",
    icon: UsersIcon,
    description: "Información de propietarios",
  },
  {
    id: 4,
    title: "Información Económica del Predio",
    shortTitle: "Información Económica",
    icon: DollarSignIcon,
    description: "Avalúos e impuestos",
  },
  {
    id: 5,
    title: "Documentos del Predio",
    shortTitle: "Documentos del Predio",
    icon: FolderIcon,
    description: "Soporte documental",
  },
  {
    id: 6,
    title: "Georeferenciación del Predio",
    shortTitle: "Georeferenciación",
    icon: MapPinIcon,
    description: "Coordenadas espaciales",
  },
] as const

export type Step = (typeof steps)[number]
