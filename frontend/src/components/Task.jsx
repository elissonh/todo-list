import RadioButtonUncheckedIcon from '@mui/icons-material/RadioButtonUnchecked';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CloseIcon from '@mui/icons-material/Close';

export default function Task({id, children, isDone, onToggle, onDelete, ...props}){
    return (
        <div className={`task ${isDone ? 'done' : ''}`}>
            <button
                className='check-button'
                onClick={() => onToggle(id)}>
                {isDone
                    ? <CheckCircleIcon fontSize="small" /> 
                    : <RadioButtonUncheckedIcon fontSize="small"/>
                }
            </button>
            <p>{children}</p>
            <button
                className="delete-task-button"
                onClick={() => onDelete(id)}>
                <CloseIcon fontSize="small"/>
            </button>
        </div>
    )
}