export default function OrderStatusBadge({ status }) {
    const map = {
        PENDING:  { label: "Pending",  cls: "badge-warning" },
        APPROVED: { label: "Approved", cls: "badge-success" },
        REJECTED: { label: "Rejected", cls: "badge-danger"  },
        SHIPPED:  { label: "Shipped",  cls: "badge-info"    },
    };
    const { label, cls } = map[status] ?? { label: status, cls: "" };
    return <span className={`badge ${cls}`}>{label}</span>;
}