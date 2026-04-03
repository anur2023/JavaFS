import { useState, useEffect } from 'react'
import { quizApi } from '../../api/modules'
import { PageHeader, EmptyState, useToast, Spinner } from '../../components/ui'

export default function Quiz() {
  const [questions, setQuestions] = useState([])
  const [responses, setResponses] = useState([])
  const [loading, setLoading] = useState(true)
  const [answering, setAnswering] = useState(null)
  const [tab, setTab] = useState('quiz')
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchAll() }, [])

  const fetchAll = async () => {
    setLoading(true)
    try {
      const [qRes, rRes] = await Promise.all([quizApi.getAll(), quizApi.getMyResponses()])
      setQuestions(Array.isArray(qRes.data) ? qRes.data : [])
      setResponses(Array.isArray(rRes.data) ? rRes.data : [])
    } catch { show('Failed to load quiz', 'error') }
    finally { setLoading(false) }
  }

  const answeredIds = new Set(responses.map(r => r.quizId))

  const handleAnswer = async (quizId, option) => {
    setAnswering(quizId)
    try {
      const { data } = await quizApi.submit({ quizId, selectedOption: option })
      setResponses(prev => [...prev, data])
      show(`Answered: Option ${option}`)
    } catch (e) { show(e.response?.data?.message || 'Failed to submit', 'error') }
    finally { setAnswering(null) }
  }

  const OPTIONS = ['A', 'B', 'C', 'D']
  const optionKeys = { A: 'optionA', B: 'optionB', C: 'optionC', D: 'optionD' }

  const getMyAnswer = (quizId) => responses.find(r => r.quizId === quizId)?.selectedOption

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <PageHeader label="Personalization" title="Style Quiz"
        subtitle="Answer questions to help us understand your fashion preferences" />

      {/* Progress */}
      {questions.length > 0 && (
        <div className="mb-8">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-mono text-charcoal-700/50">
              {responses.filter(r => questions.some(q => q.quizId === r.quizId)).length} / {questions.length} answered
            </span>
            <span className="text-xs font-mono text-gold-600">
              {Math.round((responses.filter(r => questions.some(q => q.quizId === r.quizId)).length / questions.length) * 100)}% complete
            </span>
          </div>
          <div className="h-1.5 bg-cream-200 w-full">
            <div className="h-full bg-gold-500 transition-all duration-500"
              style={{ width: `${(responses.filter(r => questions.some(q => q.quizId === r.quizId)).length / questions.length) * 100}%` }} />
          </div>
        </div>
      )}

      {/* Tabs */}
      <div className="flex gap-1 mb-8 border-b border-charcoal-900/10">
        {[['quiz', 'Take Quiz'], ['responses', `My Responses (${responses.length})`]].map(([key, label]) => (
          <button key={key} onClick={() => setTab(key)}
            className={`px-5 py-2.5 text-sm font-body font-medium transition-all duration-150 border-b-2 -mb-px ${
              tab === key ? 'border-gold-500 text-charcoal-900' : 'border-transparent text-charcoal-700/50 hover:text-charcoal-900'
            }`}>{label}
          </button>
        ))}
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : tab === 'quiz'
          ? questions.length === 0
            ? <EmptyState icon="◈" title="No questions yet" subtitle="Check back soon!" />
            : <div className="space-y-6">
                {questions.map((q, idx) => {
                  const answered = answeredIds.has(q.quizId)
                  const myAns = getMyAnswer(q.quizId)
                  return (
                    <div key={q.quizId} className={`card p-6 ${answered ? 'border-gold-400/30' : ''}`}>
                      <div className="flex items-start gap-3 mb-4">
                        <span className="font-mono text-xs text-gold-500 flex-shrink-0 mt-1">Q{idx + 1}</span>
                        <p className="font-display text-base text-charcoal-900 leading-snug">{q.questionText}</p>
                        {answered && <span className="badge-green ml-auto flex-shrink-0">Answered</span>}
                      </div>
                      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                        {OPTIONS.map(opt => (
                          <button key={opt} onClick={() => !answered && handleAnswer(q.quizId, opt)}
                            disabled={answered || answering === q.quizId}
                            className={`flex gap-3 items-center px-4 py-3 text-left text-sm transition-all duration-150 border
                              ${answered && myAns === opt
                                ? 'border-gold-500 bg-gold-400/10 text-charcoal-900 font-medium'
                                : answered
                                  ? 'border-charcoal-900/10 text-charcoal-700/40 cursor-default'
                                  : 'border-charcoal-900/15 hover:border-gold-400 hover:bg-gold-400/5 text-charcoal-700/80'
                              }`}>
                            <span className={`w-6 h-6 flex-shrink-0 flex items-center justify-center text-xs font-mono border
                              ${answered && myAns === opt ? 'border-gold-500 bg-gold-500 text-white' : 'border-current'}`}>
                              {opt}
                            </span>
                            {q[optionKeys[opt]]}
                          </button>
                        ))}
                      </div>
                      {answering === q.quizId && <div className="mt-3 flex justify-center"><Spinner size="sm" /></div>}
                    </div>
                  )
                })}
              </div>
          : responses.length === 0
            ? <EmptyState icon="◈" title="No responses yet" subtitle="Take the quiz to discover your style" action={<button onClick={() => setTab('quiz')} className="btn-primary">Start Quiz</button>} />
            : <div className="space-y-3">
                {responses.map(r => (
                  <div key={r.responseId} className="card p-4 flex items-center gap-4">
                    <div className="w-8 h-8 bg-gold-400/15 flex items-center justify-center font-mono text-sm text-gold-600 flex-shrink-0">{r.selectedOption}</div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm text-charcoal-900 truncate">{r.questionText}</p>
                      <p className="text-xs text-charcoal-700/40 mt-0.5">{new Date(r.timestamp).toLocaleString('en-IN')}</p>
                    </div>
                  </div>
                ))}
              </div>
      }
    </div>
  )
}
