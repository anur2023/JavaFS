import { useState, useEffect } from 'react'
import { quizApi } from '../../api/modules'
import { PageHeader, useToast, Spinner, Modal, FormField, EmptyState } from '../../components/ui'

const EMPTY = { questionText: '', optionA: '', optionB: '', optionC: '', optionD: '' }

export default function AdminQuiz() {
  const [questions, setQuestions] = useState([])
  const [loading, setLoading] = useState(true)
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState(EMPTY)
  const [saving, setSaving] = useState(false)
  const [deletingId, setDeletingId] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetch() }, [])

  const fetch = async () => {
    setLoading(true)
    try { const { data } = await quizApi.getAll(); setQuestions(Array.isArray(data) ? data : []) }
    catch { show('Failed to load questions', 'error') }
    finally { setLoading(false) }
  }

  const handleSave = async (e) => {
    e.preventDefault()
    if (!form.questionText || !form.optionA || !form.optionB || !form.optionC || !form.optionD)
      return show('All fields are required', 'error')
    setSaving(true)
    try {
      await quizApi.create(form)
      show('Question added!')
      setModal(false)
      setForm(EMPTY)
      fetch()
    } catch (e) { show(e.response?.data?.message || 'Save failed', 'error') }
    finally { setSaving(false) }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this question?')) return
    setDeletingId(id)
    try { await quizApi.delete(id); setQuestions(prev => prev.filter(q => q.quizId !== id)); show('Deleted') }
    catch (e) { show(e.response?.data?.message || 'Delete failed', 'error') }
    finally { setDeletingId(null) }
  }

  const f = (k) => ({ value: form[k], onChange: e => setForm(p => ({ ...p, [k]: e.target.value })) })

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="Personalization" title="Style Quiz" subtitle="Manage quiz questions shown to customers" />
        <button onClick={() => { setForm(EMPTY); setModal(true) }} className="btn-primary">+ Add Question</button>
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : questions.length === 0
          ? <EmptyState icon="◈" title="No questions" subtitle="Add your first style quiz question"
              action={<button onClick={() => setModal(true)} className="btn-primary">Add Question</button>} />
          : <div className="space-y-4">
              {questions.map((q, idx) => (
                <div key={q.quizId} className="card p-5">
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex gap-3 flex-1">
                      <span className="font-mono text-sm text-gold-500 flex-shrink-0">Q{idx + 1}</span>
                      <div className="flex-1">
                        <p className="font-display text-base text-charcoal-900 mb-3">{q.questionText}</p>
                        <div className="grid grid-cols-2 gap-2">
                          {['A', 'B', 'C', 'D'].map(opt => (
                            <div key={opt} className="flex gap-2 items-center">
                              <span className="w-5 h-5 flex items-center justify-center text-xs border border-charcoal-900/20 font-mono text-charcoal-700/60 flex-shrink-0">{opt}</span>
                              <span className="text-sm text-charcoal-700/70">{q[`option${opt}`]}</span>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>
                    <button onClick={() => handleDelete(q.quizId)} disabled={deletingId === q.quizId}
                      className="text-xs font-mono text-red-400 hover:text-red-600 flex-shrink-0 disabled:opacity-40">
                      {deletingId === q.quizId ? '…' : 'Delete'}
                    </button>
                  </div>
                </div>
              ))}
            </div>
      }

      {modal && (
        <Modal title="Add New Question" onClose={() => setModal(false)} wide>
          <form onSubmit={handleSave} className="space-y-4">
            <FormField label="Question *">
              <textarea className="input-field resize-none" rows={2} placeholder="e.g. Which best describes your style?" {...f('questionText')} required />
            </FormField>
            <div className="grid grid-cols-2 gap-3">
              {['A', 'B', 'C', 'D'].map(opt => (
                <FormField key={opt} label={`Option ${opt} *`}>
                  <input className="input-field" placeholder={`Option ${opt}…`} {...f(`option${opt}`)} required />
                </FormField>
              ))}
            </div>
            <button type="submit" disabled={saving} className="btn-primary w-full flex items-center justify-center gap-2">
              {saving ? <><Spinner size="sm" /> Adding…</> : 'Add Question'}
            </button>
          </form>
        </Modal>
      )}
    </div>
  )
}
